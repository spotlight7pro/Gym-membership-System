import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

abstract class MembershipPlan {
    public abstract double getFee();
    public abstract int getDurationMonths();
    public abstract String getPlanName();
}

class MonthlyPlan extends MembershipPlan {
    public double getFee() { return 1000.0; }
    public int getDurationMonths() { return 1; }
    public String getPlanName() { return "Monthly"; }
}

class QuarterlyPlan extends MembershipPlan {
    public double getFee() { return 2700.0; }
    public int getDurationMonths() { return 3; }
    public String getPlanName() { return "Quarterly"; }
}

class YearlyPlan extends MembershipPlan {
    public double getFee() { return 9600.0; }
    public int getDurationMonths() { return 12; }
    public String getPlanName() { return "Yearly"; }
}

// ---------------- NEW: Trainer class ----------------
class Trainer {
    private int trainerId;
    private String name;
    private int age;
    private String phone;
    private String specialization;   // e.g. Weight Training, Yoga, Cardio
    private int experienceYears;
    private String certification;    // e.g. ACE, NASM
    private String shift;            // e.g. Morning, Evening
    private LocalDate joiningDate;
    private ArrayList<Integer> assignedMemberIds = new ArrayList<>();

    public Trainer(int id, String name, int age, String phone,
                   String specialization, int experienceYears,
                   String certification, String shift) {
        this.trainerId = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.certification = certification;
        this.shift = shift;
        this.joiningDate = LocalDate.now();
    }

    public int getTrainerId() { return trainerId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    public void assignMember(int memberId) {
        if (!assignedMemberIds.contains(memberId)) {
            assignedMemberIds.add(memberId);
        }
    }

    public void removeMember(int memberId) {
        assignedMemberIds.remove(Integer.valueOf(memberId));
    }

    // Short line used when showing a member's trainer
    public String getSummary() {
        return name + " (ID " + trainerId + ", " + specialization + ")";
    }

    // Full trainer description
    public void display() {
        System.out.println("Trainer ID     : " + trainerId);
        System.out.println("Name           : " + name);
        System.out.println("Age            : " + age);
        System.out.println("Phone          : " + phone);
        System.out.println("Specialization : " + specialization);
        System.out.println("Experience     : " + experienceYears + " year(s)");
        System.out.println("Certification  : " + certification);
        System.out.println("Shift          : " + shift);
        System.out.println("Joined On      : " + joiningDate);
        System.out.println("Members Assigned: " + assignedMemberIds.size()
                + (assignedMemberIds.isEmpty() ? "" : " " + assignedMemberIds));
        System.out.println("--------------------------------");
    }
}

class Member {
    private int memberId;
    private String name;
    private int age;
    private String phone;
    private MembershipPlan plan;
    private LocalDate startDate;
    private Trainer trainer;   // NEW: assigned trainer (can be null)

    public Member(int id, String name, int age, String phone,
                  MembershipPlan plan) {
        this.memberId = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.plan = plan;
        this.startDate = LocalDate.now();
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public void updateDetails(String name, int age, String phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    // Renewal keeps the member's details and trainer, only the plan and start date change
    public void renewPlan(MembershipPlan newPlan) {
        this.plan = newPlan;
        this.startDate = LocalDate.now();
    }

    public LocalDate getExpiryDate() {
        return startDate.plusMonths(plan.getDurationMonths());
    }

    public String getStatus() {
        return LocalDate.now().isBefore(getExpiryDate())
                ? "Active" : "Expired";
    }

    public void display() {
        System.out.println("Member ID : " + memberId);
        System.out.println("Name      : " + name);
        System.out.println("Age       : " + age);
        System.out.println("Phone     : " + phone);
        System.out.println("Plan      : " + plan.getPlanName());
        System.out.println("Fee       : Rs. " + plan.getFee());
        System.out.println("Start Date: " + startDate);
        System.out.println("Valid Till: " + getExpiryDate());
        System.out.println("Status    : " + getStatus());
        System.out.println("Trainer   : "
                + (trainer == null ? "Not assigned" : trainer.getSummary()));
        System.out.println("--------------------------------");
    }
}

public class GymMembershipSystem {
    private static ArrayList<Member> members = new ArrayList<>();
    private static ArrayList<Trainer> trainers = new ArrayList<>();   // NEW
    private static Scanner sc = new Scanner(System.in);
    private static int nextId = 1001;
    private static int nextTrainerId = 2001;                          // NEW

    private static MembershipPlan choosePlan() {
        System.out.println("1. Monthly   - Rs. 1000");
        System.out.println("2. Quarterly - Rs. 2700");
        System.out.println("3. Yearly    - Rs. 9600");
        System.out.print("Choose plan: ");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1: return new MonthlyPlan();
            case 2: return new QuarterlyPlan();
            case 3: return new YearlyPlan();
            default:
                System.out.println("Invalid plan. Monthly selected.");
                return new MonthlyPlan();
        }
    }

    private static void registerMember() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        MembershipPlan plan = choosePlan();
        Member member = new Member(nextId++, name, age, phone, plan);
        members.add(member);
        System.out.println("Member registered. ID: "
                + member.getMemberId());
    }

    private static Member findMember(int id) {
        for (Member m : members) {
            if (m.getMemberId() == id) return m;
        }
        return null;
    }

    private static void displayAll() {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        for (Member m : members) m.display();
    }

    private static void searchMember() {
        System.out.print("Enter member ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Member m = findMember(id);
        if (m == null) System.out.println("Member not found.");
        else m.display();
    }

    private static void updateMember() {
        System.out.print("Enter member ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();
        m.updateDetails(name, age, phone);
        System.out.println("Member information updated.");
    }

    private static void renewMembership() {
        System.out.print("Enter member ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.println("Select the new plan:");
        MembershipPlan newPlan = choosePlan();
        m.renewPlan(newPlan);
        System.out.println("Membership renewed with new plan.");
        m.display();
    }

    // ---------------- NEW: Trainer operations ----------------

    private static Trainer findTrainer(int id) {
        for (Trainer t : trainers) {
            if (t.getTrainerId() == id) return t;
        }
        return null;
    }

    private static void registerTrainer() {
        System.out.print("Enter trainer name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        System.out.print("Enter specialization (e.g. Weight Training, Yoga, Cardio): ");
        String specialization = sc.nextLine();
        System.out.print("Enter years of experience: ");
        int experience = Integer.parseInt(sc.nextLine());
        System.out.print("Enter certification: ");
        String certification = sc.nextLine();
        System.out.print("Enter shift (Morning/Evening): ");
        String shift = sc.nextLine();

        Trainer t = new Trainer(nextTrainerId++, name, age, phone,
                specialization, experience, certification, shift);
        trainers.add(t);
        System.out.println("Trainer registered. ID: " + t.getTrainerId());
    }

    private static void displayAllTrainers() {
        if (trainers.isEmpty()) {
            System.out.println("No trainers registered.");
            return;
        }
        for (Trainer t : trainers) t.display();
    }

    private static void searchTrainer() {
        System.out.print("Enter trainer ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Trainer t = findTrainer(id);
        if (t == null) System.out.println("Trainer not found.");
        else t.display();
    }

    private static void assignTrainer() {
        if (trainers.isEmpty()) {
            System.out.println("No trainers available. Register a trainer first.");
            return;
        }
        System.out.print("Enter member ID: ");
        int memberId = Integer.parseInt(sc.nextLine());
        Member m = findMember(memberId);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.println("Available trainers:");
        for (Trainer t : trainers) {
            System.out.println(t.getTrainerId() + " - " + t.getSummary());
        }
        System.out.print("Enter trainer ID: ");
        int trainerId = Integer.parseInt(sc.nextLine());
        Trainer newTrainer = findTrainer(trainerId);
        if (newTrainer == null) {
            System.out.println("Trainer not found.");
            return;
        }

        // If the member already had a trainer, remove them from the old trainer's list
        Trainer oldTrainer = m.getTrainer();
        if (oldTrainer != null) oldTrainer.removeMember(m.getMemberId());

        m.setTrainer(newTrainer);
        newTrainer.assignMember(m.getMemberId());
        System.out.println(newTrainer.getName() + " assigned to " + m.getName() + ".");
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== GYM MEMBERSHIP SYSTEM ===");
            System.out.println("1. Register member");
            System.out.println("2. Display all members");
            System.out.println("3. Search member");
            System.out.println("4. Update member information");
            System.out.println("5. Renew membership");
            System.out.println("6. Register trainer");
            System.out.println("7. Display all trainers");
            System.out.println("8. Search trainer (description)");
            System.out.println("9. Assign trainer to member");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1: registerMember(); break;
                case 2: displayAll(); break;
                case 3: searchMember(); break;
                case 4: updateMember(); break;
                case 5: renewMembership(); break;
                case 6: registerTrainer(); break;
                case 7: displayAllTrainers(); break;
                case 8: searchTrainer(); break;
                case 9: assignTrainer(); break;
                case 10:
                    System.out.println("Thank you.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
