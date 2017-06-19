package com.dianping.mobile.framework.normalize;

import com.dianping.mobile.framework.datatypes.IMobileContext;

public interface IRequestNormalizer<T> {

	public T normalize(IMobileContext context, Class<T> clazz);
}
