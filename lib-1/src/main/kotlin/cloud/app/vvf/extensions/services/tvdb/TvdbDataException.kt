package cloud.app.vvf.extensions.services.tvdb

class TvdbDataException @JvmOverloads constructor(message: String?, throwable: Throwable? = null) :
    TvdbException(message, throwable, Service.DATA, false)
