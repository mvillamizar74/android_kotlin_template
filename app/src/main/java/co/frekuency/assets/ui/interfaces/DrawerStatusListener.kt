package co.frekuency.assets.ui.interfaces

interface DrawerStatusListener {
    fun isDrawerOpen(): Boolean
    fun openDrawer()
    fun closeDrawer()
    fun lockDrawer()
    fun unlockDrawer()
}