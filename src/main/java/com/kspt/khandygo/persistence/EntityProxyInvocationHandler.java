package com.kspt.khandygo.persistence;

import lombok.AllArgsConstructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@AllArgsConstructor
public class EntityProxyInvocationHandler<T> implements InvocationHandler {

  private final Finder finder;

  private final int id;

  private final Class<T> delegateClazz;

  private Object delegate = null;

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args)
  throws Throwable {
    if (delegate == null)
      delegate = finder.find(delegateClazz).where().eq("id", id).unique();
    return method.invoke(delegate, args);
  }
}
