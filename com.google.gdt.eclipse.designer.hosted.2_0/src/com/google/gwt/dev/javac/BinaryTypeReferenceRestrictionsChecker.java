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

import com.google.gwt.dev.jdt.TypeRefVisitor;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.NormalAnnotation;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Check a {@link CompilationUnitDeclaration} for references to binary types
 * outside the context of an annotation.
 */
public class BinaryTypeReferenceRestrictionsChecker {
  /**
   * Records the location from which a {@link BinaryTypeBinding} is referenced.
   */
  static class BinaryTypeReferenceSite {
    private final BinaryTypeBinding binaryTypeBinding;
    private final Expression expression;

    BinaryTypeReferenceSite(Expression expression,
        BinaryTypeBinding binaryTypeBinding) {
      this.expression = expression;
      this.binaryTypeBinding = binaryTypeBinding;
    }

    public BinaryTypeBinding getBinaryTypeBinding() {
      return binaryTypeBinding;
    }

    public Expression getExpression() {
      return expression;
    }
  }

  /**
   * Visits a {@link CompilationUnitDeclaration} and records all expressions
   * which use a {@link BinaryTypeBinding} that are not part of an annotation
   * context.
   */
  static class BinaryTypeReferenceVisitor extends TypeRefVisitor {
    private final List<BinaryTypeReferenceSite> binaryTypeReferenceSites;

    public BinaryTypeReferenceVisitor(CompilationUnitDeclaration cud,
        List<BinaryTypeReferenceSite> binaryTypeReferenceSites) {
      super(cud);
      this.binaryTypeReferenceSites = binaryTypeReferenceSites;
    }

    @Override
    public boolean visit(MarkerAnnotation annotation, BlockScope scope) {
      // Ignore annotations
      return false;
    }

    @Override
    public boolean visit(NormalAnnotation annotation, BlockScope scope) {
      // Ignore annotations
      return false;
    }

    @Override
    public boolean visit(SingleMemberAnnotation annotation, BlockScope scope) {
      // Ignore annotations
      return false;
    }

    @Override
    protected void onBinaryTypeRef(BinaryTypeBinding binding,
        CompilationUnitDeclaration unitOfReferrer, Expression expression) {
      binaryTypeReferenceSites.add(new BinaryTypeReferenceSite(expression,
          binding));
    }

    @Override
    protected void onTypeRef(SourceTypeBinding referencedType,
        CompilationUnitDeclaration unitOfReferrer) {
      // do nothing
    }
  }

  /**
   * Scans a {@link CompilationUnitDeclaration} for expressions that use
   * {@link BinaryTypeBinding}s outside the context of an annotation. An error
   * is reported against the {@link CompilationUnitDeclaration} for the first
   * instance of each unique {@link BinaryTypeBinding}.
   */
  public static void check(CompilationUnitDeclaration cud) {
    List<BinaryTypeReferenceSite> binaryTypeReferenceSites = findAllBinaryTypeReferenceSites(cud);
    Set<BinaryTypeBinding> alreadySeenTypeBindings = new HashSet<BinaryTypeBinding>();

    for (BinaryTypeReferenceSite binaryTypeReferenceSite : binaryTypeReferenceSites) {
      BinaryTypeBinding binaryTypeBinding = binaryTypeReferenceSite.getBinaryTypeBinding();
      if (alreadySeenTypeBindings.contains(binaryTypeBinding)) {
        continue;
      }
      alreadySeenTypeBindings.add(binaryTypeBinding);

      String fileName = String.valueOf(binaryTypeBinding.getFileName());
      if (fileName.endsWith(".java")) {
        // This binary name is valid; it is a reference to a unit that was
        // compiled in a previous JDT run.
        continue;
      }
      String qualifiedTypeName = binaryTypeBinding.debugName();
      String error = formatBinaryTypeRefErrorMessage(qualifiedTypeName);

      // TODO(mmendez): provide extra help info?
      GWTProblem.recordError(binaryTypeReferenceSite.getExpression(), cud,
          error, null);
    }
  }

  static List<BinaryTypeReferenceSite> findAllBinaryTypeReferenceSites(
      CompilationUnitDeclaration cud) {
    List<BinaryTypeReferenceSite> binaryTypeReferenceSites = new ArrayList<BinaryTypeReferenceSite>();
    BinaryTypeReferenceVisitor binaryTypeReferenceVisitor = new BinaryTypeReferenceVisitor(
        cud, binaryTypeReferenceSites);
    cud.traverse(binaryTypeReferenceVisitor, cud.scope);
    return binaryTypeReferenceSites;
  }

  static String formatBinaryTypeRefErrorMessage(String qualifiedTypeName) {
    return "No source code is available for type " + qualifiedTypeName
        + "; did you forget to inherit a required module?";
  }

  private BinaryTypeReferenceRestrictionsChecker() {
    // Not instantiable
  }
}
