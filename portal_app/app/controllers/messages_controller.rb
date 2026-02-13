class MessagesController < ApplicationController
  before_action :authenticate_user!

def create
    target = params[:conversation_id]

    if target.blank?
      return redirect_back fallback_location: root_path, alert: "Παρακαλώ επιλέξτε παραλήπτη."
    end

    if target.start_with?("user_")
      user_id = target.split("_").last
      @conversation = find_or_create_1v1_conversation(current_user.id, user_id)
    else
      @conversation = Conversation.find(target)
    end

    @message = @conversation.messages.build(message_params)
    @message.user = current_user

    if @message.save
      # === ΔΗΜΙΟΥΡΓΙΑ NOTIFICATION ===
      @conversation.users.where.not(id: current_user.id).each do |recipient|
        Notification.create(
          user: recipient,
          content: "Νέο μήνυμα από #{current_user.name.presence || current_user.email.split('@').first}: #{@message.body.truncate(30)}",
          read: false
        )
      end

      # === ΔΙΟΡΘΩΣΗ ΓΙΑ TURBO ===
      respond_to do |format|
  format.turbo_stream do
    # Αντί για advance_history, στέλνουμε μια εντολή ανανέωσης του frame
    render turbo_stream: turbo_stream.replace(
      "active_chat_messages", 
      partial: "conversations/conversation_content", 
      locals: { conversation: @conversation, messages: @conversation.messages.order(created_at: :asc) }
    )
  end
  format.html { redirect_to conversation_path(@conversation), notice: "Το μήνυμα στάλθηκε!" }
end
    end
  end
  private

  def message_params
    params.require(:message).permit(:body)
  end

  # Βοηθητική μέθοδος για να βρίσκει ή να δημιουργεί chat 1-προς-1
  def find_or_create_1v1_conversation(user1_id, user2_id)
    # Αναζήτηση συνομιλίας που δεν ανήκει σε Post και έχει ακριβώς αυτούς τους 2 χρήστες
    conv = Conversation.joins(:participations)
                       .where(post_id: nil)
                       .where(participations: { user_id: [user1_id, user2_id] })
                       .group('conversations.id')
                       .having('count(conversations.id) = 2')
                       .first
    
    if conv.nil?
      conv = Conversation.create!
      conv.users << User.find(user1_id)
      conv.users << User.find(user2_id)
    end
    conv
  end
end