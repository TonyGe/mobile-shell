/*
 * Create Author  : zhaogang.lv
 * Create Date     : 2011-6-14
 * Project            : dp-searcher
 * File Name        : MonitorServlet.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */
package com.dianping.mobile.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.avatar.log.AvatarLogger;
import com.dianping.avatar.log.AvatarLoggerFactory;

/**
 * 
 * 功能描述: monitor servlet for F5 load balance heath check
 * <p>
 * 
 */
public class MonitorServlet extends HttpServlet {
	/**
     * 
     */
	private static final long serialVersionUID = -4988760417737254572L;

	private static final AvatarLogger LOGGER = AvatarLoggerFactory
			.getLogger(MonitorServlet.class);

	// protected static final Logger LOGGER =
	// LoggerFactory.getLogger(MonitorServlet.class);

	private static final String OK = "ss ok";

	private static final String STOPPED = "stopped";

	private static final String STOPPED_DETECTED = "stopped detected";

	/**
	 * on system initialization, status is initializing in the beginning, after
	 * started and warm up successfully, status is set to OK
	 */
	private static String status = "initializing";

	private static final int MEGABYTE = (1024 * 1024);

	private Object lock = new Object();

	/** wait time before full gc in miniseconds */
	private int waitTime = 18000;

	/** wait time for full gc in miniseconds */
	private int waitTimeForFullGC = 12000;

	public void init() {
		status = OK;
	}

	public static void setStatusOk(String from) {
		if (StringUtils.equals(status, OK)) {
			return;
		}

		String oldStatus = status;
		status = OK;

		LOGGER.info("Status changed from " + oldStatus + " to " + status
				+ " from " + from + " at " + new Date().toString());
	}

	public static void setStopped(String from) {
		if (StringUtils.equals(status, STOPPED)) {
			return;
		}

		String oldStatus = status;
		status = STOPPED;
		LOGGER.info("Status changed from " + oldStatus + " to " + status
				+ " from " + from + " at " + new Date().toString());
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	/**
	 * change server status by action ( for stop, authentication is a must) if action present, <br>
	 * return new status if changed, return current status if not changed.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String client = request.getParameter("client");

		// url for F5 monitor: /monitorServlet?client=f5 , check contained
		// string is "ss ok"
		// url for get status in test: /monitorServlet
		// url for mark down server:
		// /monitorServlet?action=markdown&username=admin&password=admin
		// url for mark up server:
		// /monitorServlet?action=markup&username=admin&password=admin
		// after mark down, F5 will detect the status to be not ok
		// and after f5 detected the markdown, a log is written to indicate
		// status changed to STOPPED_DETECTED

		if (null == action || action.length() <= 0) // no action, just return
		// the status
		{
			response.setContentType("text/html");

			// request comes from F5, and was stopped, then set to
			// STOPPED_DETECTED
			if (status.equals(STOPPED) && null != client
					&& client.equalsIgnoreCase("f5")) {
				status = STOPPED_DETECTED;
				LOGGER.info("Status changed from " + STOPPED + " to " + status
						+ " at " + new Date().toString());
				// LOGGER.info("Status changed from {} to {} at {}", new
				// Object[]{STOPPED, status, new Date().toString()} );
			}
		} else if (null != action) {
			/*
			 * if(action.equalsIgnoreCase("showip")) // show ip address {
			 * if(null==serverStatus) { serverStatus =
			 * SearchAppListener.retrieveServerInfo(this.getServletContext(),
			 * springContext); }
			 * 
			 * response.getWriter().print(serverStatus.getIp() + ":" +
			 * serverStatus.getPort()); return; } else
			 */

			String username = "admin";
			String password = "*()IOPkl;";

			if (StringUtils.equals(username, request
					.getParameter("username"))
					&& StringUtils.equals(password, request
							.getParameter("password"))
					&& ! StringUtils.equals(action, "cat")) {
				if (action.equalsIgnoreCase("markdown")) {
					// if user authenticated
					setStopped(request.getRemoteAddr());
				} else if (action.equalsIgnoreCase("markup")) {
					setStatusOk(request.getRemoteAddr());
				} else if (action.equalsIgnoreCase("fullgc")) {
					synchronized (lock) {
						try {
							// mark down before full gc to assure coming request
							// can be done.
							setStopped(request.getRemoteAddr());

							lock.wait(waitTime);

							System.gc();

							// do not wait and reset status back to OK, let the
							// trigger to mark up the service
							// lock.wait(waitTimeForFullGC);
						} catch (InterruptedException e) {
							LOGGER
									.warn(
											"InterruptedException occur while waiting "
													+ waitTime
													+ " seconds for F5 markdown status detection",
											e);
						} 
					}
				}
			}
			// show memory information, show usedmem:maxmem on page
			else if (action.equalsIgnoreCase("showmem")) {
				MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
				MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
				long maxMemory = heapUsage.getMax() / MEGABYTE;
				long usedMemory = heapUsage.getUsed() / MEGABYTE;
				response.getWriter().print(usedMemory + ":" + maxMemory);
				return;
			} else if (action.equalsIgnoreCase("showua")) {
				// TODO user agent is not collected any more, plan to remove it
				response.getWriter().println("size: 0");
				return;
			} 
		}

		PrintWriter out = response.getWriter();
		// return "search ok" on normal condition
		String responseMessage = status;
		// set status to "stopped" if stop action sent with password

		out.print(responseMessage);

	}

	/**
	 * getter method
	 * 
	 * @see MonitorServlet#waitTime
	 * @return the waitTime
	 */
	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * setter method
	 * 
	 * @see MonitorServlet#waitTime
	 * @param waitTime
	 *            the waitTime to set
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * getter method
	 * 
	 * @see MonitorServlet#waitTimeForFullGC
	 * @return the waitTimeForFullGC
	 */
	public int getWaitTimeForFullGC() {
		return waitTimeForFullGC;
	}

	/**
	 * setter method
	 * 
	 * @see MonitorServlet#waitTimeForFullGC
	 * @param waitTimeForFullGC
	 *            the waitTimeForFullGC to set
	 */
	public void setWaitTimeForFullGC(int waitTimeForFullGC) {
		this.waitTimeForFullGC = waitTimeForFullGC;
	}

	public static String getServerStatus() {
		return status;
	}
}