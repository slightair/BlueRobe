using UnityEngine;

public class CharacterControl : MonoBehaviour
{
    private Rigidbody rigidBody;
    private Animator animator;

    // Use this for initialization
    void Start()
    {
        rigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void Update()
    {
        AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);
        if (Input.GetMouseButtonDown(0))
        {
            if (isGrounded() && stateInfo.IsName("Hikari.Run"))
            {
                animator.SetBool("PrepareJump", true);
            }
        }
        else if (Input.GetMouseButtonUp(0))
        {
            if (isGrounded())
            {
                animator.SetBool("PrepareJump", false);
                rigidBody.AddForce(Vector3.up * 10, ForceMode.VelocityChange);
            }
        }
    }

    private bool isGrounded()
    {
        return transform.position.y < 1;
    }
}
