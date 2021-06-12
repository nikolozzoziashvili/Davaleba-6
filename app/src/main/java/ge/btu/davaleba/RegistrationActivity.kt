package ge.btu.davaleba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmationEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null){
            goToMain()
        }
        setContentView(R.layout.activity_registration)
        this.init()
        this.registerListener()
    }
    private fun init(){
        emailEditText = findViewById(R.id.editTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextPassword)
        passwordConfirmationEditText = findViewById(R.id.editTextPasswordConfirmation)
        submitButton = findViewById(R.id.submit)
    }
    private fun registerListener(){
        submitButton.setOnClickListener{
            val email: String = emailEditText.text.toString()
            val password: String = passwordEditText.text.toString()
            val passwordConfirmation: String = passwordConfirmationEditText.text.toString()
            if (email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
                Toast.makeText(this, "Forms should not be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isEmailValid(email)){
                Toast.makeText(this, "Email format is incorrect!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != passwordConfirmation){
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isPasswordValid(password)){
                Toast.makeText(this, "Password does not meet requirements!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
                if (task.isSuccessful) {
                    goToMain()
                }
                else{
                    Toast.makeText(this, "Not Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isPasswordValid(password: String): Boolean{
        val reg = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{9,}".toRegex()
        return reg.matches(password)
    }
    private fun goToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}