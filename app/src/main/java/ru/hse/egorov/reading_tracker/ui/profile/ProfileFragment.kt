package ru.hse.egorov.reading_tracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.login.SignUpSignInActivity

class ProfileFragment : Fragment(), ActionBarSetter {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBar(activity as AppCompatActivity)

        view.exit.setOnClickListener {
            val intent = Intent(activity, SignUpSignInActivity::class.java)
            startActivity(intent)
            dbManager.signOut()
            activity?.finish()
        }
    }

    override fun setActionBar(activity: AppCompatActivity) {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        activity.supportActionBar?.setCustomView(R.layout.profile_action_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        setHasOptionsMenu(false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * *
         */
        fun newInstance() = ProfileFragment()
    }
}
