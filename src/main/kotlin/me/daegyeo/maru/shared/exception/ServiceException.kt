package me.daegyeo.maru.shared.exception

import me.daegyeo.maru.shared.error.BaseError

class ServiceException(
    val error: BaseError,
) : RuntimeException()
