class Users::OmniauthCallbacksController < Devise::OmniauthCallbacksController
  # Μέθοδος για Google Login
  def google_oauth2
    handle_auth("Google")
  end

  # Μέθοδος για Facebook Login
  def facebook
    handle_auth("Facebook")
  end

  private

  def handle_auth(kind)
    # Η μέθοδος from_omniauth πρέπει να υπάρχει στο μοντέλο User.rb
    @user = User.from_omniauth(request.env["omniauth.auth"])

    if @user.persisted?
      flash[:notice] = "Επιτυχής σύνδεση μέσω #{kind}!"
      sign_in_and_redirect @user, event: :authentication
    else
      # Αν αποτύχει, στέλνει τον χρήστη στην εγγραφή με τα σφάλματα
      session["devise.#{kind.downcase}_data"] = request.env["omniauth.auth"].except(:extra)
      redirect_to new_user_registration_url, alert: @user.errors.full_messages.join("\n")
    end
  end
end