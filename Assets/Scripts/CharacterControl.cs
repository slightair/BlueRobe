using UnityEngine;

public class CharacterControl : MonoBehaviour
{
    private static float MoveRatio = 0.05f;
    private static float MoveMax = 0.8f;
    private static float JumpPower = 10f;
    private static float ForwardSpeed = 0.5f;

    private Rigidbody rigidBody;
    private Animator animator;
    private bool isJumpCancelled;
    private Vector3 move;

    // Use this for initialization
    void Start()
    {
        rigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        move = transform.position;
        move += transform.forward * ForwardSpeed;

        processTouches();

        rigidBody.MovePosition(move);
    }

    private bool isGrounded()
    {
        // TODO
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
                move += transform.right * Mathf.Clamp(deltaX * MoveRatio, -MoveMax, MoveMax);
            }
        }
        else if (touch.phase == TouchPhase.Ended || touch.phase == TouchPhase.Canceled)
        {
            animator.SetBool("PrepareJump", false);
            if (isGrounded() && !isJumpCancelled)
            {
                rigidBody.AddForce(Vector3.up * JumpPower, ForceMode.VelocityChange);
            }
        }
    }
}
