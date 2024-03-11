package com.steve_md.smartmkulima.data.repositories.impl

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.steve_md.smartmkulima.data.repositories.AuthRepository
import com.steve_md.smartmkulima.model.User
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl : AuthRepository{

    private  val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private  val firebaseAuth: FirebaseAuth =  FirebaseAuth.getInstance()

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall{
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(uid,email,username,password,confirmPassword)
                databaseReference.child(uid).setValue(user).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO){
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                Resource.Success(result)
            }
        }
    }
}