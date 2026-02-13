class ApplicationController < ActionController::Base
  # Επιτρέπει τα έξτρα πεδία του Devise
  before_action :configure_permitted_parameters, if: :devise_controller?
  
  # ΑΥΤΟ ΠΡΟΣΘΕΤΟΥΜΕ: Βρίσκει τη συνομιλία για το Chat Popup
  before_action :set_active_conversation

  protected

  def configure_permitted_parameters
    devise_parameter_sanitizer.permit(:sign_up, keys: [:name, :interests, :courses])
    devise_parameter_sanitizer.permit(:account_update, keys: [:name, :interests, :courses])
  end

  private

  def set_active_conversation
    if user_signed_in?
      # 1. Αν είμαστε ήδη σε σελίδα συνομιλίας, δείξε αυτήν
      if params[:controller] == 'conversations' && params[:id]
        @conversation = Conversation.find_by(id: params[:id])
      # 2. Αλλιώς, πάρε την πιο πρόσφατη συνομιλία του χρήστη για να μην είναι άδειο το popup
      else
        @conversation = current_user.conversations.order(updated_at: :desc).first
      end
    end
  end
end