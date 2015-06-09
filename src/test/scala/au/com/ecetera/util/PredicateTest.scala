package au.com.ecetera.util

import org.specs2._

/*
 * ====
 *     Copyright 2015 Ecetera Pty Ltd
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * ====
 *
 * (c) Copyright Ecetera Pty Ltd, 2015
 *
 * Some files contain other unattributed Contributions to the Work; All Contributions
 * received from Contributors under the terms of the Apache License Agreement v 2.0 and
 * re-distributed in accordance with that license.
 */



/**
 * Created by robertk on 4/04/15.
 */
class PredicateSpec extends PredicateTest {
 def is = s2"""
     Predicates

     compose lazily with && $e1
     compose lazily with || $e2
     """


}

trait PredicateTest extends Specification {
  def e1 = todo
  def e2 = todo
}
