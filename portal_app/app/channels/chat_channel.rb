class ChatChannel < ApplicationCable::Channel
  def subscribed
    # stream_from "some_channel"
    stream_from "chat_channel"
    
    # Ακούει στο προσωπικό κανάλι του χρήστη (για προσωπικά μηνύματα)
    stream_from "chat_user_#{params[:user_id]}" if params[:user_id].present?
  end

  def unsubscribed
    # Any cleanup needed when channel is unsubscribed
  end
end
