/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.google.gdt.eclipse.designer.model.widgets;

import com.google.gdt.eclipse.designer.model.widgets.support.GwtState;

import org.eclipse.wb.core.model.IRootProcessor;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.broadcast.EditorActivatedListener;
import org.eclipse.wb.core.model.broadcast.EditorActivatedRequest;

import java.util.List;

/**
 * Support for {@link UIObjectInfo} features.
 * 
 * @author scheglov_ke
 * @coverage gwt.model
 */
public final class UIObjectRootProcessor implements IRootProcessor {
  ////////////////////////////////////////////////////////////////////////////
  //
  // Instance
  //
  ////////////////////////////////////////////////////////////////////////////
  public static final IRootProcessor INSTANCE = new UIObjectRootProcessor();

  private UIObjectRootProcessor() {
  }

  ////////////////////////////////////////////////////////////////////////////
  //
  // IRootProcessor
  //
  ////////////////////////////////////////////////////////////////////////////
  public void process(JavaInfo root, List<JavaInfo> components) throws Exception {
    if (root instanceof UIObjectInfo) {
      UIObjectInfo object = (UIObjectInfo) root;
      reparseOnStateModification(object);
      addButtonForCSS(object);
    }
  }

  private void reparseOnStateModification(UIObjectInfo object) {
    final GwtState state = object.getState();
    object.addBroadcastListener(new EditorActivatedListener() {
      public void invoke(EditorActivatedRequest request) throws Exception {
        if (state.isModified()) {
          request.requestRefresh();
        }
        state.activate();
      }
    });
  }

  /**
   * Adds listener for adding CSS editor button on toolbar.
   */
  private void addButtonForCSS(UIObjectInfo object) {
    object.addBroadcastListener(new SelectionActionsSupport(object));
  }
}
