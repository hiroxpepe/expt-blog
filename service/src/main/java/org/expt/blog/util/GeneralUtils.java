/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.expt.blog.util;

import java.util.Random;
import org.apache.commons.lang.StringUtils;

/**
 * @author h.adachi
 */
public class GeneralUtils {

    private static Random random = new Random();

    public static String createCode() {
        int intCode = random.nextInt(999999999);
        String strCode = String.valueOf(intCode);
        String code = StringUtils.leftPad(strCode, 9, "0");
        return code;
    }
}
