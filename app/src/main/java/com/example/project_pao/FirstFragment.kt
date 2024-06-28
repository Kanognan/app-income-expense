package com.example.project_pao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.example.project_pao.databinding.ActivityMainBinding
import com.example.project_pao.databinding.FragmentFirstBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirstFragment : Fragment() {

    private lateinit var bindingFirst: FragmentFirstBinding
    lateinit var session: SessionManager
    val createClient = userApi.create()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        bindingFirst = FragmentFirstBinding.inflate(layoutInflater)
        bindingFirst.btnRegister.setOnClickListener() {
            var fragment: Fragment? = null
            fragment = SecondFragment()
            replaceFragment(fragment)
        }
        bindingFirst.Login.setOnClickListener() {
            createClient.login(
                bindingFirst.loginEmail.text.toString(),
                bindingFirst.loginPassword.text.toString()
            ).enqueue(object : Callback<user> {
                override fun onResponse(
                    call: Call<user>,
                    response: retrofit2.Response<user>
                ) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(context, "Successfully login", Toast.LENGTH_SHORT).show()
                        val id = (response.body()?.us_id)
                        val email = (response.body()?.us_email)
                        createClient.getwalletID(id.toString()).enqueue(object : Callback<wallet> {
                            override fun onResponse(
                                call: Call<wallet>,
                                response: retrofit2.Response<wallet>
                            ) {
                                if (response.isSuccessful()) {
                                    val wallet_id = (response.body()?.wallet_id.toString())
                                    session = SessionManager(requireContext())
                                    session.createLoginSession(id.toString(), email.toString(),wallet_id.toString())
                                } else {
                                    Toast.makeText(context, "no id wallet ", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<wallet>, t: Throwable) {
                                Toast.makeText(
                                    context,
                                    "Error onFailure getwalletID " + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })

                        var i: Intent = Intent(context, MainActivity::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(context, "กรุณากรอกข้อมูลที่ถูกต้อง", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<user>, t: Throwable) {
                    Toast.makeText(context, "Error onFailure " + t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        // Inflate the layout for this fragment
        return bindingFirst.root
    }

    fun replaceFragment(someFragment: Fragment) {
        var binding: ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        var transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(binding.frameLayout.id, someFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}