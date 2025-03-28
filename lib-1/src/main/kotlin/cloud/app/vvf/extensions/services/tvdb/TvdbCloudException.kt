package cloud.app.vvf.extensions.services.tvdb


class TvdbCloudException(message: String?, throwable: Throwable?) :
    TvdbException(message, throwable, Service.HEXAGON, false)
