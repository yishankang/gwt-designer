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
package com.google.gwt.dev.javac;

import com.google.gwt.dev.resource.Resource;

import org.eclipse.jdt.core.compiler.CategorizedProblem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A compilation unit that was generated.
 */
class SourceFileCompilationUnit extends CompilationUnitImpl {

  /**
   * A token to retrieve this object's bytes from the disk cache. It's generally
   * much faster to read from the disk cache than to reread individual
   * resources.
   */
  private long cacheToken = -1;

  private final Resource sourceFile;

  private final ContentId contentId;

  public SourceFileCompilationUnit(Resource sourceFile, ContentId contentId,
      List<CompiledClass> compiledClasses, Set<ContentId> dependencies,
      Collection<? extends JsniMethod> jsniMethods,
      MethodArgNamesLookup methodArgs, CategorizedProblem[] problems) {
    super(compiledClasses, dependencies, jsniMethods, methodArgs, problems);
    this.sourceFile = sourceFile;
    this.contentId = contentId;
  }

  @Override
  public String getDisplayLocation() {
    return sourceFile.getLocation();
  }

  @Override
  public long getLastModified() {
    return sourceFile.getLastModified();
  }

  @Deprecated
  @Override
  public String getSource() {
    if (cacheToken < 0) {
      String sourceCode = Shared.readSource(sourceFile);
      cacheToken = diskCache.writeString(sourceCode);
      return sourceCode;
    } else {
      return diskCache.readString(cacheToken);
    }
  }

  public Resource getSourceFile() {
    return sourceFile;
  }

  @Override
  public String getTypeName() {
    return Shared.getTypeName(sourceFile);
  }

  @Deprecated
  @Override
  public boolean isGenerated() {
    return false;
  }

  @Deprecated
  @Override
  public boolean isSuperSource() {
    return sourceFile.wasRerooted();
  }

  @Override
  ContentId getContentId() {
    return contentId;
  }
}
