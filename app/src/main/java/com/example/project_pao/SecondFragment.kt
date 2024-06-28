package com.example.project_pao

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project_pao.databinding.ActivityMainBinding
import com.example.project_pao.databinding.FragmentFirstBinding
import com.example.project_pao.databinding.FragmentSecondBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SecondFragment : Fragment() {
    private lateinit var bindingSecond: FragmentSecondBinding
    val createClient = userApi.create()
    var id = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingSecond = FragmentSecondBinding.inflate(layoutInflater)
        bindingSecond.btnLogin.setOnClickListener() {
            var fragment: Fragment? = null
            fragment = FirstFragment()
            replaceFragment(fragment)
        }
        bindingSecond.register.setOnClickListener() {
            if(bindingSecond.regisEmail.text.toString().isNotEmpty() && bindingSecond.regisPassword.text.toString().isNotEmpty() &&
                bindingSecond.regisPassword2.text.toString().isNotEmpty()){
                val password1 = bindingSecond.regisPassword.text.toString()
                val password2 = bindingSecond.regisPassword2.text.toString()

                if (password1 == password2) {

                    createClient.insertuser(
                        bindingSecond.regisEmail.text.toString(),
                        bindingSecond.regisPassword.text.toString()
                    ).enqueue(object : Callback<user> {
                        override fun onResponse(
                            call: Call<user>,
                            response: retrofit2.Response<user>
                        ) {
                            if (response.isSuccessful()) {
                                ////
                                createClient.login(
                                    bindingSecond.regisEmail.text.toString(),
                                    bindingSecond.regisPassword.text.toString()
                                ).enqueue(object : Callback<user> {
                                    override fun onResponse(
                                        call: Call<user>,
                                        response: retrofit2.Response<user>
                                    ) {
                                        if (response.isSuccessful()) {
                                            id = (response.body()?.us_id.toString())
                                            insertwallet()

                                        } else {
                                            Toast.makeText(context, "not ready", Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }

                                    override fun onFailure(call: Call<user>, t: Throwable) {
                                        Toast.makeText(
                                            context,
                                            "Error onFailure " + t.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                })
                                Toast.makeText(context, "สมัครสมาชิกเสร็จสิ้น", Toast.LENGTH_SHORT)
                                    .show()
                                insertwallet()

                            } else {
                                Toast.makeText(context, "Error ", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<user>, t: Throwable) {
                            Toast.makeText(context, "Error onFailure " + t.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                } else {
                    Toast.makeText(context, "รหัสผ่านไม่ตรงกัน ", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบ ", Toast.LENGTH_SHORT).show()
            }

        }
        // Inflate the layout for this fragment
        return bindingSecond.root
    }

    fun insertwallet() {
        createClient.insertwallet(
            id
        ).enqueue(object : Callback<wallet> {
            override fun onResponse(
                call: Call<wallet>, responsey: Response<wallet>
            ) {
                if (responsey.isSuccessful()) {
//                    Toast.makeText(context, "created wallet", Toast.LENGTH_SHORT).show()
                    var Fragment: Fragment? = null
                    Fragment = FirstFragment()
                    replaceFragment(Fragment)
                } else {
                    Toast.makeText(context, "Error wallet ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<wallet>, t: Throwable) {
                Toast.makeText(context, "Error onFailure wallet " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
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