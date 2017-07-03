package biz.eventually.atpl.ui.source

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.AdapterView
import biz.eventually.atpl.AtplApplication
import biz.eventually.atpl.BuildConfig
import biz.eventually.atpl.R
import biz.eventually.atpl.common.IntentIdentifier
import biz.eventually.atpl.common.StateIdentifier
import biz.eventually.atpl.data.model.Source
import biz.eventually.atpl.ui.BaseActivity
import biz.eventually.atpl.ui.subject.SubjectActivity
import kotlinx.android.synthetic.main.activity_source.*

class SourceActivity : BaseActivity<SourceManager>() {

    private var mAdapter: SourceAdapter? = null
    private var mSourceList: List<Source>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AtplApplication.component.inject(this)
        setContentView(R.layout.activity_source)

        app_version.text = "v${BuildConfig.VERSION_APP}"

        // font for the title
        setWelcomeFont()

        savedInstanceState?.let {
            mSourceList = it.getParcelableArrayList<Source>(StateIdentifier.SOURCE_LIST).toList()
        }

        mSourceList = mSourceList ?: intent.getParcelableArrayListExtra<Source>(IntentIdentifier.SOURCE_LIST)

        when(intent.getBooleanExtra(IntentIdentifier.NETWORK_ERROR, false)) {
            true -> {
                source_error.visibility = GONE
                onError()
            }
            false -> {
                mSourceList?.let {
                    displayData(it)
                } ?: run {
                    loadData()
                }
            }
        }

        source_refresh.setOnClickListener {
            loadData()
        }
    }

    private fun setWelcomeFont() {
        val tangerine = Typeface.createFromAsset(assets, "fonts/Tangerine.ttf")
        source_welcome.typeface = tangerine
    }

    override fun onPause() {
        super.onPause()
    }

    /*
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        mSourceList?.let {
            outState?.putParcelableArrayList(StateIdentifier.SOURCE_LIST, it as? ArrayList<Subject>)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }*/

    /*
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            mSourceList = it.getParcelableArrayList(StateIdentifier.SOURCE_LIST)
            displayData(mSourceList)
        }
    }
    */

    private fun displayData(sources: List<Source>?): Unit {

        sources?.apply {
            mAdapter = SourceAdapter(this@SourceActivity, sources)

            source_listview.adapter = mAdapter
            source_listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val intent = Intent(this@SourceActivity, SubjectActivity::class.java)
                intent.putExtra(IntentIdentifier.SOURCE_ID, get(position).id)
                intent.putExtra(IntentIdentifier.SOURCE_NAME, get(position).name)
                startActivity(intent)
            }
            rotateloading.stop()
        }
    }

    private fun loadData() {
        showHideError(View.GONE)
        rotateloading.start()
        manager.getSources(this::displayData, this::onError)
    }

    private fun showHideError(show: Int) {
        source_error.visibility = show
        source_refresh.visibility = show
    }

    private fun onError(): Unit {
        rotateloading.stop()
        showHideError(View.VISIBLE)
    }
}
