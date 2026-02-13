Rails.application.routes.draw do
  get "conversations/index"
  get "conversations/show"
  # 1. Ορίζει την αρχική σελίδα (Root)
  root "posts#index"

  # 2. Αυθεντικοποίηση (Devise) - Προσθήκη Omniauth για Google Login
  devise_for :users, controllers: { omniauth_callbacks: 'users/omniauth_callbacks' }

  # 3. Βασικές λειτουργίες (Resources)
  resources :posts
  resources :friendships, only: [:create, :destroy]
  resources :messages, only: [:create]
  resources :participations, only: [:create]
  resources :conversations do
  resources :messages, only: [:create]
end

  # 4. Health check
  get "up" => "rails/health#show", as: :rails_health_check
end