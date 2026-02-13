import consumer from "channels/consumer"

// Παίρνουμε το ID του τρέχοντος χρήστη από το meta tag που βάλαμε στο layout
const currentUserId = document.querySelector('meta[name="current-user-id"]')?.getAttribute('content');

consumer.subscriptions.create({ channel: "ChatChannel", user_id: currentUserId }, {
  connected() {
    console.log("Connected to ChatChannel for user: " + currentUserId);
  },

  disconnected() {
    // Κλήση όταν η σύνδεση διακοπεί
  },

  received(data) {
    const messagesDiv = document.getElementById("messages");
    
    // Δημιουργία του μηνύματος στο UI
    if (messagesDiv) {
      const newMessage = `
        <div style="margin-bottom: 8px; border-bottom: 1px solid #f0f0f0;">
          <small style="color: #888;">${new Date().toLocaleTimeString()}</small><br>
          <strong>${data.user}:</strong> ${data.body}
        </div>
      `;
      
      messagesDiv.insertAdjacentHTML('beforeend', newMessage);
      
      // Auto-scroll στο τέλος του chat
      messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }

    // --- ΖΗΤΟΥΜΕΝΟ 5: Notification ---
    // Εμφάνιση ειδοποίησης αν ο χρήστης δεν βλέπει εκείνη τη στιγμή τον browser
    if (document.hidden && Notification.permission === "granted") {
      new Notification("Lab Portal: Νέο μήνυμα", {
        body: `${data.user}: ${data.body}`,
        icon: "/icon.png" // Αν έχεις εικονίδιο
      });
    }
  }
});