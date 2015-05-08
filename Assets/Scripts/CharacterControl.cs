using UnityEngine;

public class CharacterControl : MonoBehaviour
{
    private Rigidbody rigidBody;
    private Animator animator;
    private bool isJumpCancelled;

    // Use this for initialization
    void Start()
    {
        rigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void Update()
    {
        processTouches();

//         Vector3 newPosition = transform.position;
//         newPosition.x -= 0.5f;
//         transform.position = newPosition;
    }

    private bool isGrounded()
    {
        return transform.position.y < 1;
    }

    private void processTouches()
    {
        if (Input.touchCount == 0)
        {
            return;
        }

        AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);

        Touch touch = Input.GetTouch(0);
        if (touch.phase == TouchPhase.Began)
        {
            isJumpCancelled = false;
            if (isGrounded() && stateInfo.IsName("Hikari.Run"))
            {
                animator.SetBool("PrepareJump", true);
            }
        }
        else if (touch.phase == TouchPhase.Moved)
        {
            isJumpCancelled = true;
            float deltaX = touch.deltaPosition.x;
            if (deltaX != 0)
            {
                if (stateInfo.IsName("Hikari.PrepareJump"))
                {
                    animator.SetBool("PrepareJump", false);
                }

                Vector3 newPosition = transform.position;
                newPosition.z += deltaX * 0.05f;
                transform.position = newPosition;
            }
        }
        else if (touch.phase == TouchPhase.Ended || touch.phase == TouchPhase.Canceled)
        {
            animator.SetBool("PrepareJump", false);
            if (isGrounded() && !isJumpCancelled)
            {
                rigidBody.AddForce(Vector3.up * 10, ForceMode.VelocityChange);
            }
        }
    }
}
