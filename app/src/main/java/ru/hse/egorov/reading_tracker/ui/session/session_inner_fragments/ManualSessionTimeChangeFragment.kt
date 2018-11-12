package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manual_session_time_change.view.*

import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher


class ManualSessionTimeChangeFragment : Fragment(), FragmentLauncher {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_manual_session_time_change, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.sessionTime.setIs24HourView(true)

        view.toSession.setOnClickListener {
            val args = Bundle()
            args.putInt("minutes", view.sessionTime.hour * 60 + view.sessionTime.minute)
            val newFrag = AutoSessionTimeChangeFragment.newInstance()
            newFrag.arguments = args
            openInnerFragment(newFrag, parentFragment!!, R.id.sessionFragment)
        }
    }

    companion object {
        fun newInstance() = ManualSessionTimeChangeFragment()
    }
}
