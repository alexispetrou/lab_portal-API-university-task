class ConversationsController < ApplicationController
  before_action :authenticate_user!

def show
  @conversation = Conversation.find(params[:id])
  @messages = @conversation.messages.order(created_at: :asc)
  @new_message = Message.new

  respond_to do |format|
    format.html {
      if turbo_frame_request?
        # Αν το ζητάει το Frame, στείλε μόνο τα μηνύματα
        render partial: 'conversations/conversation_content', locals: { conversation: @conversation, messages: @messages }
      else
        # Αν μπαίνεις κανονικά από το URL, δείξε όλη τη σελίδα (αν έχεις τέτοια σελίδα)
        render :show
      end
    }
  end
end
end