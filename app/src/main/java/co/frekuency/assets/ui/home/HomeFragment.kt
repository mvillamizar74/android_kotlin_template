package co.frekuency.assets.ui.home

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import co.frekuency.assets.R
import co.frekuency.assets.data.utilities.KeyCodePressEvent
import co.frekuency.assets.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: KeyCodePressEvent) {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            dialog(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dialog(context: Context){
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.exit_or_sign_out_app_msg))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                // Do nothing
            }
            .setNegativeButton(resources.getString(R.string.sign_out)) { _, _ ->
                navController.popBackStack(R.id.nav_home,inclusive = true)
                navController.navigate(R.id.nav_login)

            }
            .setPositiveButton(resources.getString(R.string.exit)) { _, _ ->
                requireActivity().finishAndRemoveTask()
            }
            .show()
    }
}