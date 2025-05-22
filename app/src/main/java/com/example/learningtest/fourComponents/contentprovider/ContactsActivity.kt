package com.example.learningtest.fourComponents.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsActivity : ComponentActivity() {
    private val snackbarHostState = SnackbarHostState()
    private var contactsListState by mutableStateOf<List<SimpleContactItem>>(emptyList())
    private var permissionGrantedState by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                ContactsScreen(
                    snackbarHostState = snackbarHostState,
                    contacts = contactsListState,
                    permissionGranted = permissionGrantedState,
                    onRequestPermission = { requestContactsPermission() },
                    onRefreshContacts = {
                        if (permissionGrantedState) {
                            loadContacts()
                        }
                    },
                )
            }
        }
        checkAndRequestContactsPermission()
    }

    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            REQUEST_CONTACT_PERMISSION_CODE,
        )
    }

    private fun checkAndRequestContactsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS,
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGrantedState = true
                loadContacts()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS,
            ) -> {
                permissionGrantedState = false
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar("연락처를 표시하려면 권한이 필요합니다. 다시 요청합니다.")
                }
                requestContactsPermission()
            }

            else -> {
                permissionGrantedState = false
                requestContactsPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_CONTACT_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGrantedState = true
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar("연락처 접근 권한이 허용되었습니다.")
                }
                loadContacts()
            } else {
                permissionGrantedState = false
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar("권한이 거부되었습니다. 연락처를 표시할 수 없습니다.")
                }
                contactsListState = emptyList()
            }
        }
    }

    private fun loadContacts() {
        if (!permissionGrantedState) {
            Log.d("ContactsActivity", "권한이 없어 연락처를 로드할 수 없습니다.")
            contactsListState = emptyList()
            return
        }

        lifecycleScope.launch {
            Log.d("ContactsActivity", "연락처 로드 시작...")
            val fetchedContacts =
                withContext(Dispatchers.IO) {
                    val contactList = mutableListOf<SimpleContactItem>()
                    val contactProjection =
                        arrayOf(
                            ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                            ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        )
                    val sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"

                    val contactCursor =
                        contentResolver.query(
                            ContactsContract.Contacts.CONTENT_URI,
                            contactProjection,
                            null,
                            null,
                            sortOrder,
                        )

                    contactCursor?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                        val nameColumn =
                            cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                        val hasPhoneNumberColumn =
                            cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idColumn)
                            val name = cursor.getString(nameColumn)
                            val hasPhoneNumber =
                                cursor.getInt(hasPhoneNumberColumn) > 0

                            var phoneNumber = "번호 없음"
                            if (name != null && hasPhoneNumber) {
                                val phoneCursor =
                                    contentResolver.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                                        arrayOf(id.toString()),
                                        null,
                                    )

                                phoneCursor?.use { pCursor ->
                                    if (pCursor.moveToFirst()) {
                                        val numberColumn =
                                            pCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                        phoneNumber = pCursor.getString(numberColumn)
                                    }
                                }
                                phoneCursor?.close()
                            }
                            if (name != null) {
                                contactList.add(SimpleContactItem(id, name, phoneNumber))
                            }
                        }
                    }
                    contactCursor?.close()
                    Log.d("ContactsActivity", "${contactList.size}개의 연락처 로드 완료.")
                    contactList
                }
            contactsListState = fetchedContacts
        }
    }

    companion object {
        private const val REQUEST_CONTACT_PERMISSION_CODE = 1001
    }
}

data class SimpleContactItem(val id: Long, val name: String, val number: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    snackbarHostState: SnackbarHostState,
    contacts: List<SimpleContactItem>,
    permissionGranted: Boolean,
    onRequestPermission: () -> Unit,
    onRefreshContacts: () -> Unit,
) {
    LaunchedEffect(key1 = permissionGranted) {
        if (permissionGranted) {
            onRefreshContacts()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("간단한 연락처 목록") },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
        ) {
            if (!permissionGranted) {
                Text("연락처를 표시하려면 권한이 필요합니다.")

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = onRequestPermission) {
                    Text("권한 요청하기")
                }
            } else if (contacts.isEmpty()) {
                Text("연락처가 없거나 아직 로드되지 않았습니다.")
            } else {
                contacts.forEach { contact ->
                    ContactRowSimple(contact = contact)
                }
            }
        }
    }
}

@Composable
fun ContactRowSimple(
    contact: SimpleContactItem,
    modifier: Modifier = Modifier,
) {
    Row {
        Text(
            text = contact.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(vertical = 8.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = contact.number,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(vertical = 8.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenSimplePreview() {
    val sampleContacts =
        listOf(
            SimpleContactItem(1, "홍길동 (미리보기)"),
            SimpleContactItem(2, "김영희 (미리보기)"),
        )
    MaterialTheme {
        ContactsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            contacts = sampleContacts,
            permissionGranted = true,
            onRequestPermission = {},
            onRefreshContacts = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenSimpleNoPermissionPreview() {
    MaterialTheme {
        ContactsScreen(
            snackbarHostState = remember { SnackbarHostState() },
            contacts = emptyList(),
            permissionGranted = false,
            onRequestPermission = {},
            onRefreshContacts = {},
        )
    }
}
