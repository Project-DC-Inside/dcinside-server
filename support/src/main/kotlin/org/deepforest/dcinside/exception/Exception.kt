package org.deepforest.dcinside.exception

import org.deepforest.dcinside.dto.ErrorCode

class ApiException(val errorCode: ErrorCode) : Throwable()
