package co.frekuency.assets.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import co.frekuency.assets.R
import co.frekuency.assets.data.utilities.Status
import co.frekuency.assets.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)

        email = binding.emailTxtInputLayout
        password = binding.passwordTxtInputLayout
        button = binding.loginButton
        val progressBar = binding.progressBar

        email.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (email.editText?.text?.isNotEmpty() == true) {
                    email.error = String()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        password.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (password.editText?.text?.isNotEmpty() == true) {
                    password.error = String()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        button.setOnClickListener {
            if (email.editText?.text.isNullOrEmpty()) {
                email.error = getString(R.string.field_required_error_msg)
            } else if(password.editText?.text.isNullOrEmpty()){
                password.error = getString(R.string.field_required_error_msg)
            }else{
                viewModel.login(
                    email.editText?.text.toString(),
                    password.editText?.text.toString()
                )
            }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    //valid user
                    progressBar.visibility = View.GONE
                    navController.popBackStack(R.id.nav_login,inclusive = true)
                    navController.navigate(R.id.nav_home)
                    //enableUi(true)
                }
                Status.LOADING -> {
                    //enableUi(false)
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    //Invalid user
                    progressBar.visibility = View.GONE
                    /*Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.dismiss)){ enableUi(true) }
                        .show()*/
                }
            }
        }
    }
}