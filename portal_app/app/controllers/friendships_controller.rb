class FriendshipsController < ApplicationController
  def create
  # Δημιουργία της πρώτης κατεύθυνσης (Εγώ -> Φίλος)
  @friendship1 = current_user.friendships.build(friend_id: params[:friend_id])
  
  # Δημιουργία της δεύτερης κατεύθυνσης (Φίλος -> Εγώ)
  @friendship2 = Friendship.new(user_id: params[:friend_id], friend_id: current_user.id)

  if @friendship1.save && @friendship2.save
    redirect_to root_path, notice: "Η επαφή προστέθηκε αμφίδρομα!"
  else
    redirect_to root_path, alert: "Σφάλμα κατά την προσθήκη."
  end
end

  def destroy
  # Βρίσκουμε και τις δύο εγγραφές
  f1 = Friendship.find_by(user_id: current_user.id, friend_id: params[:id])
  f2 = Friendship.find_by(user_id: params[:id], friend_id: current_user.id)
  
  f1&.destroy
  f2&.destroy
  
  redirect_to root_path, notice: "Η επαφή αφαιρέθηκε και από τους δύο."
end
end