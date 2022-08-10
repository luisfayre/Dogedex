package com.example.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.annotation.ExperimentalCoilApi
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.databinding.ActivityDogListBinding
import com.example.dogedex.dogdetail.DogDetailComposeActivity.Companion.DOG_KEY
import com.example.dogedex.dogdetail.DogDetailComposeActivity

private const val GRID_SPAN_COUNT = 3

@ExperimentalCoilApi
class DogListActivity : AppCompatActivity() {

    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)

        var progressBar = binding.loadingProgress

        val adapter = DogAdapter()

        adapter.setOnItemClickListener {
            // Passar el dog a DogDetailActivity
            val intent = Intent(this, DogDetailComposeActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        recycler.adapter = adapter



        dogListViewModel.dogList.observe(this) { dogList ->
            adapter.submitList(dogList)
        }

        dogListViewModel.status.observe(this) { status ->

            when (status) {
                is ApiResponseStatus.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> progressBar.visibility = View.VISIBLE

                is ApiResponseStatus.Success -> progressBar.visibility = View.GONE
            }
        }
    }
}