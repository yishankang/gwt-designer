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
package com.google.gwt.dev.js.ast;

import com.google.gwt.dev.jjs.SourceInfo;

/**
 * Represents a JavaScript label statement.
 */
public class JsLabel extends JsStatement implements HasName {

  private final JsName label;

  private JsStatement stmt;

  public JsLabel(SourceInfo sourceInfo, JsName label) {
    super(sourceInfo);
    this.label = label;
  }

  public JsName getName() {
    return label;
  }

  public JsStatement getStmt() {
    return stmt;
  }

  public void setStmt(JsStatement stmt) {
    this.stmt = stmt;
  }
  public void traverse(JsVisitor v, JsContext<JsStatement> ctx) {
    if (v.visit(this, ctx)) {
      stmt = v.accept(stmt);
    }
    v.endVisit(this, ctx);
  }
}
