package ir.fallahpoor.enlightened.presentation.newsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import ir.fallahpoor.enlightened.R
import kotlinx.android.synthetic.main.fragment_news_details.*

class NewsDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_news_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        newsDetailsWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
            }
        }
        newsDetailsWebView.setBackgroundColor(resources.getColor(R.color.backgroundPrimary))
        newsDetailsWebView.loadUrl(getNewsUrl())

    }

    private fun getNewsUrl(): String? {
        return arguments?.let {
            NewsDetailsFragmentArgs.fromBundle(it).newsUrl
        }
    }

}
