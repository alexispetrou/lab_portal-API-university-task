class PostsController < ApplicationController
  # 1. Απαίτηση login για όλες τις ενέργειες
  before_action :authenticate_user! 
  
  before_action :set_post, only: [:show, :edit, :update, :destroy]

  before_action :authorize_user!, only: [:edit, :update, :destroy]
  


  def index
    @posts = Post.all
  end

  def show
  end

  def new
    @post = Post.new
  end

  def edit
  end

  def create
    # 2. Σύνδεση του post με τον τρέχοντα χρήστη
    @post = current_user.posts.build(post_params)

    respond_to do |format|
      if @post.save
        format.html { redirect_to @post, notice: "Post was successfully created." }
        format.json { render :show, status: :created, location: @post }
      else
        format.html { render :new, status: :unprocessable_entity }
        format.json { render json: @post.errors, status: :unprocessable_entity }
      end
    end
  end

  def update
    respond_to do |format|
      if @post.update(post_params)
        format.html { redirect_to @post, notice: "Post was successfully updated.", status: :see_other }
        format.json { render :show, status: :ok, location: @post }
      else
        format.html { render :edit, status: :unprocessable_entity }
        format.json { render json: @post.errors, status: :unprocessable_entity }
      end
    end
  end

  def destroy
    @post.destroy!
    respond_to do |format|
      format.html { redirect_to posts_path, notice: "Post was successfully destroyed.", status: :see_other }
      format.json { head :no_content }
    end
  end

  private
    def set_post
      @post = Post.find(params[:id]) # Αν σου βγάλει error το .expect, χρησιμοποίησε [:id]
    end

    def authorize_user!
      if @post.user != current_user
        redirect_to posts_path, alert: "Δεν έχετε δικαίωμα να επεξεργαστείτε αυτό το post!"
      end
    end

    def post_params
      # Αφαιρούμε το :user_id από εδώ γιατί το παίρνουμε αυτόματα από το current_user
      params.require(:post).permit(:title, :content, :category)
    end
end