/**
 * 
 */
package com.dianping.mobile.framework.common.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dianping.mobile.framework.annotation.MobileDo.MobileField;

/**
 * @author kewen.yao
 *
 */
@SuppressWarnings("serial")
public abstract class ResultList<LType> implements Serializable {
	
	protected ResultList() {
		this.list = new ArrayList<LType>();
	}
	
	@MobileField(key = 0x177d)
	protected int recordCount; 
	
	@MobileField(key = 0xaa64)
	private int startIndex; 
	
	@MobileField(key = 0xf0b)
	private boolean isEnd; 
	
	@MobileField(key = 0x249a)
	private List<LType> list;	
	
	@MobileField(key = 0x5703)
	private int nextStartIndex;
	
	@SuppressWarnings("unused")
	@MobileField(key = 0x7503)
	private int nextStartIndex_bk;//容错，客户端5.6之前的字段值
	
	@MobileField(key = 0xa465)
	private String emptyMsg;
	
	@MobileField(key = 0x2d87)
	private String queryId = "-1";
	
	private boolean judgeSetList = false;
	private boolean judgeSetStart = false;
	
	
	public String getQueryId() {
		return queryId;
	}


	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	 
	public int getNextStartIndex() {
		return nextStartIndex;
	}

	
	public int getRecordCount() {
		return recordCount;
	}


	protected void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}


	public int getStartIndex() {
		return startIndex;
	}


	protected void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		judgeSetStart = true;
		autoChangeNextStartIndex();
	}

	public boolean isIsEnd() {
		return isEnd;
	}


	protected void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}


	public List<LType> getList() {
		return list;
	}


	public void setList(List<LType> list) {
		this.list = list;	
		this.judgeSetList = true;
		autoChangeNextStartIndex();
	}
	
	
	public String getEmptyMsg() {
		return emptyMsg;
	}


	public void setEmptyMsg(String emptyMsg) {
		this.emptyMsg = emptyMsg;
	}
	
	private void autoChangeNextStartIndex() {
		if(judgeSetStart && judgeSetList
				   && (this.nextStartIndex <= this.startIndex)
				   && this.list != null
				   && this.list.size() > 0) {
			this.nextStartIndex = this.nextStartIndex_bk = this.startIndex + this.list.size();
		}		
	}
}
