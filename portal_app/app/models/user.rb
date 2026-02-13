class User < ApplicationRecord
  # Include default devise modules.
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :validatable,
         :omniauthable, omniauth_providers: [:google_oauth2,:facebook]

  has_many :posts, dependent: :destroy
  has_many :friendships, dependent: :destroy
  has_many :friends, through: :friendships, source: :friend
  has_many :messages, dependent: :destroy
  has_many :participations
  has_many :conversations, through: :participations
  has_many :notifications, dependent: :destroy

  def self.from_omniauth(auth)
  # Πρώτα ψάχνουμε αν υπάρχει χρήστης με αυτό το email
  user = User.find_by(email: auth.info.email)

  if user
    # Αν υπάρχει, ενημερώνουμε το uid και το provider για να συνδεθεί ο λογαριασμός
    user.update(uid: auth.uid, provider: auth.provider)
    return user
  else
    # Αν δεν υπάρχει, τον δημιουργούμε από το μηδέν
    where(provider: auth.provider, uid: auth.uid).first_or_create do |new_user|
      new_user.email = auth.info.email
      new_user.password = Devise.friendly_token[0, 20]
      new_user.name = auth.info.name
    end
  end
end
end