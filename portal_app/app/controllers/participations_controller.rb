class ParticipationsController < ApplicationController
  before_action :authenticate_user!

  def create
    # 1. Βρίσκει τη συνομιλία που ανήκει στο Post
    @conversation = Conversation.find(params[:conversation_id])
    
    # 2. Προσθέτει τον τωρινό χρήστη στη λίστα των μελών της ομάδας (αν δεν είναι ήδη)
    unless @conversation.users.include?(current_user)
      @conversation.users << current_user
    end

    # 3. Σε στέλνει πίσω στην αρχική σελίδα με ένα μήνυμα επιτυχίας
    # (Εκεί πλέον η ομάδα θα φαίνεται στη λίστα των chats σου)
    redirect_to root_path, notice: "Προστέθηκες στην ομάδα!"
  end
end