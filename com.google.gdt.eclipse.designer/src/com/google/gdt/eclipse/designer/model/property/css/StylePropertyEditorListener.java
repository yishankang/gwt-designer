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
package com.google.gdt.eclipse.designer.model.property.css;

import org.eclipse.wb.core.model.ObjectInfo;

import java.util.List;

/**
 * Broadcast listener for {@link StylePropertyEditor}.
 * 
 * @author scheglov_ke
 * @coverage gwt.model.property
 */
public class StylePropertyEditorListener {
  /**
   * Adds context-specific {@link ContextDescription} in addition to the contexts from CSS files.
   */
  public void addContextDescriptions(ObjectInfo object, List<ContextDescription> contexts)
      throws Exception {
  }
}
