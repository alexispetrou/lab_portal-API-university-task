class FixMessagesForConversations < ActiveRecord::Migration[8.1]
  def change
    # Προσθέτουμε τη σύνδεση με τη συνομιλία
    add_reference :messages, :conversation, foreign_key: true
    
    # Προαιρετικά: Αν δεν χρησιμοποιείς πια το recipient_id, μπορείς να το αφαιρέσεις
    # remove_column :messages, :recipient_id, :integer
  end
end