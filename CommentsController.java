package wewoAPI;

import com.amazonaws.services.lambda.runtime.Context;

import exceptions.UnauthorizedException;
import modelPOJO.Comment;
import modelPOJO.IDObject;

class CommentController{
	public IDObject createComment(Comment comment, Context context){
		if(context.getIdentity() == null || context.getIdentity().getIdentityId().isEmpty())
		{
			throw new UnauthorizedException();
		}
		
	}
}
