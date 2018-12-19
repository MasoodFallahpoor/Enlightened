package ir.fallahpoor.enlightened.presentation.settings

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.reactivex.disposables.Disposable
import ir.fallahpoor.enlightened.R
import ir.fallahpoor.enlightened.domain.interactor.DeleteNewsUseCase
import ir.fallahpoor.enlightened.presentation.app.App
import ir.fallahpoor.enlightened.presentation.settings.di.DaggerSettingsComponent
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    private companion object {
        const val KEY_PREFERENCE_DELETE_NEWS = "deleteNews"
        const val KEY_PREFERENCE_SAVE_NEWS = "saveNews"
    }

    @Inject
    lateinit var deleteNewsUseCase: DeleteNewsUseCase

    private var disposable: Disposable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DaggerSettingsComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)

        setVisibilityOfClearNewsItem()
        setListenerOfSaveNewsPreference()
        setListenerOfClearNewsPreference()

    }

    private fun setVisibilityOfClearNewsItem() {
        val saveNewsPreference: SwitchPreferenceCompat = findPreference(KEY_PREFERENCE_SAVE_NEWS)
        val deleteNewsPreference: Preference = findPreference(KEY_PREFERENCE_DELETE_NEWS)
        deleteNewsPreference.isVisible = saveNewsPreference.isChecked
    }

    private fun setListenerOfSaveNewsPreference() {

        val saveNewsPreference: Preference = findPreference(KEY_PREFERENCE_SAVE_NEWS)

        saveNewsPreference.setOnPreferenceClickListener {
            val deleteNewsPreference: Preference = findPreference(KEY_PREFERENCE_DELETE_NEWS)

            if ((it as SwitchPreferenceCompat).isChecked) {
                deleteNewsPreference.isVisible = true
            } else {
                deleteNewsPreference.isVisible = false
                deleteNews()
            }
            true
        }

    }

    private fun deleteNews() {
        disposable = deleteNewsUseCase.execute(Unit)
            .subscribe(
                { showToast(R.string.news_deleted) },
                { showToast(R.string.news_deletion_failed) }
            )
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun setListenerOfClearNewsPreference() {
        val deleteNewsPreference: Preference = findPreference(KEY_PREFERENCE_DELETE_NEWS)
        deleteNewsPreference.setOnPreferenceClickListener {
            showDeleteNewsConfirmationDialog()
            true
        }
    }

    private fun showDeleteNewsConfirmationDialog() {
        AlertDialog.Builder(activity!!)
            .setMessage(R.string.delete_news_confirmation_message)
            .setNegativeButton(R.string.not_now, null)
            .setPositiveButton(R.string.delete_news) { _: DialogInterface, _: Int ->
                deleteNews()
            }
            .create()
            .show()
    }

}
