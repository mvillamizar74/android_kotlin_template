package co.frekuency.assets.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.frekuency.assets.data.utilities.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val _message: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val message: LiveData<Resource<Boolean>> get() = _message

    fun login(email: String, password: String) {
        _message.value = Resource.loading(data = null)
        CoroutineScope(Dispatchers.IO).launch {
            delay(3 * 1000L)
            setResource(Resource.success(data = true))
        }
    }

    private suspend fun setResource(resource: Resource<Boolean>){
        withContext(Main) {
            _message.value = resource
        }
    }
}