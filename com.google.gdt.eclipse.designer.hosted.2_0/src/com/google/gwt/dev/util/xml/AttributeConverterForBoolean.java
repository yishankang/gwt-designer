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
package com.google.gwt.dev.util.xml;

import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Subclass for converting strings into Integer.
 */
public class AttributeConverterForBoolean extends AttributeConverter {
  public Object convertToArg(Schema schema, int line, String elem,
      String attr, String value) throws UnableToCompleteException {
    if (value.equals("true")) {
      return Boolean.TRUE;
    } else if (value.equals("false")) {
      return Boolean.FALSE;
    } else {
      schema.onBadAttributeValue(line, elem, attr, value, Boolean.class);
    }
    return null;
  }
}
