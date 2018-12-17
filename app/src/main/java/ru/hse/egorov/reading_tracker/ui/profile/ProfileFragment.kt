package ru.hse.egorov.reading_tracker.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import ru.hse.egorov.reading_tracker.R

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
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
