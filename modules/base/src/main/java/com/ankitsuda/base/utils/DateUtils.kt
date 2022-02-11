/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.base.utils

import java.time.*


fun LocalDateTime.toEpochMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
fun Long.toLocalDateTime(): LocalDateTime? =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault());

fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toEpochMillis() =
    this.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

val YearMonth.next: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previous: YearMonth
    get() = this.minusMonths(1)