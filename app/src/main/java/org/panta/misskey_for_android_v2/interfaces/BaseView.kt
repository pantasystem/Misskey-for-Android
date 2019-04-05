package org.panta.misskey_for_android_v2.interfaces

interface BaseView<T: BasePresenter> {
    var mPresenter: T
}