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
package com.google.gwt.dev.jjs.ast;

import com.google.gwt.dev.jjs.SourceInfo;

import java.util.List;

/**
 * Represents a rebound entry point before deferred binding decisions are
 * finalized. Replaced with the entry call for the appropriate rebind result in
 * that permutation.
 */
public class JReboundEntryPoint extends JStatement {

  private final List<JExpression> entryCalls;
  private final List<JClassType> resultTypes;
  private final JDeclaredType sourceType;

  public JReboundEntryPoint(SourceInfo info, JDeclaredType sourceType,
      List<JClassType> resultTypes, List<JExpression> entryCalls) {
    super(info);
    this.sourceType = sourceType;
    this.resultTypes = resultTypes;
    this.entryCalls = entryCalls;
  }

  public List<JExpression> getEntryCalls() {
    return entryCalls;
  }

  public List<JClassType> getResultTypes() {
    return resultTypes;
  }

  public JDeclaredType getSourceType() {
    return sourceType;
  }

  public void traverse(JVisitor visitor, Context ctx) {
    if (visitor.visit(this, ctx)) {
      visitor.accept(entryCalls);
    }
    visitor.endVisit(this, ctx);
  }
}
