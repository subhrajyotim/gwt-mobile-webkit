/*
 * Copyright 2009 Bart Guijt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.code.gwt.storage.client;

import com.google.gwt.core.client.GWT;

/**
 * IE8-specific implementation of the Web Storage.
 * 
 * @author bguijt
 * 
 * @see <a
 *      href="http://msdn.microsoft.com/en-us/library/cc197062(VS.85).aspx">MSDN
 *      - Introduction to DOM Storage</a>
 */
public class StorageImplIE8 extends StorageImpl {

  static String eventKey;
  static String eventOldValue;
  static String eventNewValue;
  static String eventUrl;
  static Storage eventSource;

  @Override
  public void setItem(Storage storage, String key, String data) {
    String oldValue = null;
    if (storageEventHandlers.size() > 0) {
      oldValue = getItem(storage, key);
    }
    super.setItem(storage, key, data);
    if (storageEventHandlers.size() > 0) {
      prepareStorageEventData(key, oldValue, data, storage);
    }
  }

  @Override
  public void removeItem(Storage storage, String key) {
    String oldValue = null;
    if (storageEventHandlers.size() > 0) {
      oldValue = getItem(storage, key);
    }
    super.removeItem(storage, key);
    if (storageEventHandlers.size() > 0) {
      prepareStorageEventData(key, oldValue, null, storage);
    }
  }

  @Override
  public void clear(Storage storage) {
    super.clear(storage);
    if (storageEventHandlers.size() > 0) {
      prepareStorageEventData("", null, null, storage);
    }
  }

  private void prepareStorageEventData(String key, String oldValue,
      String newValue, Storage storage) {
    eventKey = key;
    eventOldValue = oldValue;
    eventNewValue = newValue;
    eventSource = storage;
    eventUrl = GWT.getHostPageBaseURL();
  }

  @Override
  protected native void addStorageEventHandler0() /*-{
    @com.google.code.gwt.storage.client.StorageImpl::jsHandler = function(event) {
      @com.google.code.gwt.storage.client.StorageImpl::handleStorageEvent(Lcom/google/code/gwt/storage/client/StorageEvent;) (event);
    };
    $doc.onstorage = @com.google.code.gwt.storage.client.StorageImpl::jsHandler;
  }-*/;

  @Override
  protected native void removeStorageEventHandler0() /*-{
    $doc.onstorage = null;
  }-*/;
}
