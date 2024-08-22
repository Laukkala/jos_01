#
# Sysconf plugin for Java Operating System Statistics JOS-01
# Copyright (C) 2021  Fail-Safe IT Solutions Oy
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#
#
# Additional permission under GNU Affero General Public License version 3
# section 7
#
# If you modify this Program, or any covered work, by linking or combining it
# with other code, such other code is not for that reason alone subject to any
# of the requirements of the GNU Affero GPL version 3 as long as this Program
# is the same Program as licensed from Fail-Safe IT Solutions Oy without any
# additional modifications.
#
# Supplemented terms under GNU Affero General Public License version 3
# section 7
#
# Origin of the software must be attributed to Fail-Safe IT Solutions Oy.
# Any modified versions must be marked as "Modified version of" The Program.
#
# Names of the licensors and authors may not be used for publicity purposes.
#
# No rights are granted for use of trade names, trademarks, or service marks
# which are in The Program if any.
#
# Licensee must indemnify licensors and authors for any liability that these
# contractual assumptions impose on licensors and authors.
#
#
AC_INIT([sysconf], 1.0)
#AC_CONFIG_MACRO_DIRS([m4]
AM_INIT_AUTOMAKE
LT_INIT
AC_PROG_CC
AC_DEFINE([_POSIX_C_SOURCE], [200809L], [Define the POSIX version])
AC_DEFINE(_BSD_SOURCE)
AC_DEFINE(HAVE_ATOMIC_BUILTINS64)
AC_CHECK_HEADERS
AC_C_INLINE
AC_FUNC_MALLOC
AC_TYPE_MODE_T
AC_TYPE_OFF_T
AC_TYPE_UINT8_T
AC_CONFIG_FILES(Makefile)
AC_OUTPUT