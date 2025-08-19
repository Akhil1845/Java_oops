// dashboard.js

// Firebase configuration (replace with yours)
const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_PROJECT_ID.appspot.com",
    messagingSenderId: "YOUR_SENDER_ID",
    appId: "YOUR_APP_ID"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();
const db = firebase.firestore();

// DOM elements
const nameEl = document.getElementById("name");
const emailEl = document.getElementById("email");

// Listen for auth state changes
auth.onAuthStateChanged(user => {
    if (user) {
        // Show email from Auth
        emailEl.textContent = user.email;

        // Fetch name from Firestore
        db.collection("students").doc(user.uid).get()
            .then(doc => {
                if (doc.exists && doc.data().name) {
                    nameEl.textContent = doc.data().name;
                } else {
                    nameEl.textContent = "Unknown"; // If no name found
                }
            })
            .catch(error => {
                console.error("Error fetching name:", error);
                nameEl.textContent = "Unknown";
            });

    } else {
        // No user → redirect to login
        window.location.href = "login.html";
    }
});

// Logout function
function logout() {
    auth.signOut()
        .then(() => {
            window.location.href = "login.html";
        })
        .catch(error => {
            console.error("Logout error:", error);
        });
}
