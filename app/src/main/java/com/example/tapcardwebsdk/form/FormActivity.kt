package com.example.tapcardwebsdk.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tapcardwebsdk.R
import it.facile.form.*
import it.facile.form.model.*
import it.facile.form.model.models.FormModel
import it.facile.form.storage.FormStorage
import it.facile.form.ui.FormRecyclerView
import it.facile.form.ui.adapters.FieldsLayouts
import it.facile.form.ui.adapters.SectionsAdapter
import it.facile.form.ui.viewmodel.SectionViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class FormActivity : AppCompatActivity() {

    private val formRecyclerView: FormRecyclerView by lazy { findViewById(R.id.singlePageForm) }
    private var sectionsAdapter: SectionsAdapter? = null


    private val formModel = TestForm.FORM_MODEL(FormStorage.empty(), Schedulers.io(), AndroidSchedulers.mainThread())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        initForm(formModel, formModel.pages[0].buildPageViewModel(formModel.storage).sections, 0)

    }

    fun initForm(formModel: FormModel, sectionViewModels: List<SectionViewModel>, pageIndex: Int) {
        formRecyclerView.formModel = formModel
        formRecyclerView.pageIndex = pageIndex
        if (sectionsAdapter == null) {
            sectionsAdapter = SectionsAdapter(
                sectionViewModels = sectionViewModels,
                fieldsLayouts = FieldsLayouts()
            )
            formRecyclerView.adapter = sectionsAdapter
        }
    }

}