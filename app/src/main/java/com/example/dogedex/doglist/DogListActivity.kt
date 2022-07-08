package com.example.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.databinding.ActivityDogListBinding
import com.example.dogedex.dogdetail.DogDetailActivity
import com.example.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

private const val GRID_SPAN_COUNT = 3

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
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        recycler.adapter = adapter

        adapter.setLongItemClickListener {
            dogListViewModel.addDogToUser(it.id)
        }

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